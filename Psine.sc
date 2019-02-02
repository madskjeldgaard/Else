Psine : Pattern{
	*new{|freq=1, phase=0|
		^Plazy({ sin(2pi * freq * Ptime() + phase) });
	}
}



