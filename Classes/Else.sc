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
		^Pn(sin(2pi * freq * Ptime() + phase)).linlin(-2pi,2pi,0.0,1.0)
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
		^Pn(cos(2pi * freq * Ptime() + phase)).linlin(-2pi,2pi,0.0,1.0)
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

/*

TIDAL inspired patterns

*/

Pspeed : Pattern {
    *new { |pat, speed=1|
		^Pchain(pat, (stretch: speed.reciprocal))
    }
}

Ptops : Pattern {
	*new { |eventPatterns, waveLength=1, overWrite=true ... params|
		var sineFunc = { |eventIndex, paramIndex, waveLength|
			Psine(( 1+paramIndex )*( 1+eventIndex )*waveLength)
		};

		^Pweave(eventPatterns, sineFunc, waveLength, overWrite, *params)
	}

}

Pwaves : Pattern {
	*new { |eventPatterns, waveLength=1, overWrite=true ... params|
		var sineFunc = { |eventIndex, paramIndex, waveLength|
			var phase = eventIndex.linlin(0,eventPatterns.size, 0.0, 2pi);
			var sine = Psine(( 1+paramIndex )*( 1+eventIndex )*waveLength, phase);

			sine.linlin(-1.0,1.0,0.0,1.0)
		};

		^Pweave(eventPatterns, sineFunc, waveLength, overWrite, *params)
	}

}

Pweave : Pattern {
	*new { |eventPatterns, weaveFunc, weaveSpeed=1, overWrite=true ... params|
		var weavePats = eventPatterns.collect { |eventPat, eventIndex|
			var newParams = params.collect { |param, paramIndex|
				var paramFunc = weaveFunc.value(eventIndex, paramIndex, weaveSpeed);

				var paramValue = Pkey(param.asSymbol) * paramFunc;

				overWrite.if {paramValue = paramFunc};

				[param.asSymbol, paramValue] 
			}.flatten;

			Pbindf(eventPat, *newParams)
		};

		^Ppar(weavePats)
	}
}
