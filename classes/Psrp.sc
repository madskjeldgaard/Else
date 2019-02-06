/*
a type of SRP - Shifting Repetitive Patterns
Psrp.sc - likes activity on the right-hand side of the decimal point
*/
Psrp : Pattern{
	*new{|freq=1, phase=0|
		^Plazy({var undulate, result;
			undulate = Psine(freq, phase) % Pcosine(freq, phase);
			result = sin(4pi / exp(undulate + phase));
			result;
		});
	}
}
