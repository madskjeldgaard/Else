Psrp : Pattern{
	*new{|freq=1, phase=0|
		^Plazy({var undulate, result;
			undulate = Psine(freq, phase) % Pcosine(freq, phase);
			result = sin(4pi / exp(undulate + phase));
			result;
		});
	}
}

Psrpmod : Pattern{
	*new{|freq=1, mod=0.5, phase=0|
		^Plazy({var undulate, wave, result;
			undulate = Psine(freq, phase) * Pcosine(mod, phase);
			wave = undulate * (Psine(mod, phase) + Pcosine(freq, phase));
			result = sin(4pi * Pcosine(wave, phase) + phase);
			result;
		});
	}
}