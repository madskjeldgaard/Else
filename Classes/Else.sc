/*

Copy and change an event pattern from a Pdef key to a new Pdef

*/

Pdeff : Pdef {
	*new { |pdefname, copyfrom ... args|
		(pdefname == copyfrom).if({ 
			"The name of this Pdef and the one copied from are the same!".error;
			^nil
		}, {
			^Pdef(pdefname, Pbindf( Pdef(copyfrom), *args))
		})
	}
}

/*

Line patterns

*/

Pexpodec : ListPattern {
    *new { |steps=10, repeats=1|
        var revxline = Array.fill(steps, { |i| i.linexp(0,steps-1,0.00001,1.0)}).reverse;

        ^Pseq(revxline, repeats)
    }
}

Prexpodec : ListPattern {
    *new { |steps=10, repeats=1|
        var xline = Array.fill(steps, { |i| i.linexp(0,steps-1,0.00001,1.0)});

        ^Pseq(xline, repeats)
    }
}

/*

Palindrome

TODO: Make an .asPalindrome method for all patterns

*/

Ppalindrome : ListPattern {
	*new { |list, repeats=1, offset=0|

		^Pseq.new(list ++ list.reverse, repeats, offset)
	}


}

/*

Sine and cosine functions

*/

// Tak til Eirik og Niklas for at fixe dette!
Psine : Pattern {
    *new { |freq=1, phase=0|

		//phase arg in radians e.g. 1pi, 1.5pi etc.
		//add 2pi the get correct freq and phase values
		//Don't use .abs here. But rather do scaling outside the pattern
		//   e.g. Psine(...).linlin(-1.0, 1.0, 0.0, 1.0)

		^Pn(sin(2pi * freq * Ptime() + phase))
    }
}

// Normalized
Psinen : Pattern {
    *new { |freq=1, phase=0|
		^Pn(sin(2pi * freq * Ptime() + phase)).linlin(-1,1,0.0,1.0)
    }
}

Pcosine : Pattern {
    *new { |freq=1, phase=0|
		^Pn(cos(2pi * freq * Ptime() + phase))
    }
}

// Normalized
Pcosinen : Pattern {
    *new { |freq=1, phase=0|
		^Pn(cos(2pi * freq * Ptime() + phase)).linlin(-1,1,0.0,1.0)
    }
}

/*

Weird Niklas math

*/

Psrp : Pattern {
	*new { |freq=1, phase=0|
		^Plazy({var undulate, result;
			undulate = Psine(freq, phase) % Pcosine(freq, phase);
			result = sin(4pi / exp(undulate + phase));
			result
		})
	}
}

// Normalized
Psrpn : Pattern {
	*new { |freq=1, phase=0|
		^Plazy({var undulate, result;
			undulate = Psine(freq, phase) % Pcosine(freq, phase);
			result = sin(4pi / exp(undulate + phase));
			result.linlin(-1,1,0.0,1.0)
		})
	}
}

Psrpmod : Pattern {
	*new { |freq=1, mod=0.5, phase=0|
		^Plazy({var undulate, wave, result;
			undulate = Psine(freq, phase) * Pcosine(mod, phase);
			wave = undulate * (Psine(mod, phase) + Pcosine(freq, phase));
			result = sin(4pi * Pcosine(wave, phase) + phase);
			result
		})
	}
}

// Normalized
Psrpmodn : Pattern {
	*new { |freq=1, mod=0.5, phase=0|
		^Plazy({var undulate, wave, result;
			undulate = Psine(freq, phase) * Pcosine(mod, phase);
			wave = undulate * (Psine(mod, phase) + Pcosine(freq, phase));
			result = sin(4pi * Pcosine(wave, phase) + phase);
			result.linlin(-1,1,0,1)
		})
	}
}

PairyDeriv : Pattern {
	*new { |freq=1, phase1=0, phase2=0.5, versions = 2 |
		^Plazy({var b1, b2, wave, result;
			b1 = {|i| Psine(freq + i/versions, phase1)}.dup(versions).sum;
			b2 = {|i| Pcosine(freq / 0.5pi + i / versions, phase2)}.dup(versions).sum;
			wave = b1 % b2;
			result = wave / versions;
			result
		})
	}
}
// Normalized
PairyDerivn : Pattern {
	*new { |freq=1, phase1=0, phase2=0.5, versions = 2 |
		^Plazy({var b1, b2, wave, result;
			b1 = {|i| Psine(freq + i/versions, phase1)}.dup(versions).sum;
			b2 = {|i| Pcosine(freq / 0.5pi + i / versions, phase2)}.dup(versions).sum;
			wave = b1 % b2;
			result = wave / versions;
			result.linlin(-1,1,0,1)
		})
	}
}

/*

TIDAL inspired patterns

*/

Pspeed : Pattern {
    *new { |pat, speed=1|
		^Pchain(pat, (stretch: speed.reciprocal))
    }
}

// Basic weave function. Can be used directly but probably shouldn't
Pweave : Pattern {
	*new { |eventPatterns, weaveSpeed=1, overWrite=true ... params|

		// For every event pattern in the list...
		var weavePats = eventPatterns.collect { |eventPat, eventIndex|

			// ... and every parameter chosen
			var newParams = params.collect { |param, paramIndex|

				// ... Use this function to create a new version of the pattern(s)
				var paramFunc = this.weaveFunc(
					eventIndex, 
					paramIndex, 
					weaveSpeed, 
					eventPatterns.size
				);

				// Possibly overwriting original parameter values, or scaling them against the function
				var paramValue = overWrite.if(
					{ paramFunc },
					{ Pkey(param.asSymbol) * paramFunc} // Scale original value using func
				);

				[param.asSymbol, paramValue] 
			}.flatten;

			// Modified version of event pattern collected
			Pbindf(eventPat, *newParams)
		};

		// All modified versions of the event patterns stacked on top of eachother again
		^Ppar(weavePats)
	}

	*weaveFunc{ |eventIndex, paramIndex, weaveSpeed, numEventPatterns|
		"Do not use the Pweave function directly".error;
		^nil
	}
}

// Weave patterns using sine, fixed phase
Ptops : Pweave {
	*weaveFunc{ |eventIndex, paramIndex, weaveSpeed, numEventPatterns|
		eventIndex.odd.if({
			^Psinen(( 1+paramIndex )*( 1+eventIndex )*weaveSpeed)
		}, {
			^Pcosinen(( 1+paramIndex )*( 1+eventIndex )*weaveSpeed)
		})
	}
}

// Weave patterns using sine, distributed phase
Pwaves : Pweave {
	*weaveFunc{ |eventIndex, paramIndex, weaveSpeed, numEventPatterns|
			var phase = eventIndex.linlin(0, numEventPatterns, -2pi, 2pi);
			^Psinen(( 1+paramIndex )*( 1+eventIndex )*weaveSpeed, phase);
	}
}

// Weave using envelopes
Psegways : Pweave {
	*weaveFunc{ |eventIndex, paramIndex, weaveSpeed, numEventPatterns|

	}
} 
