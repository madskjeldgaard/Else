Psine : Pattern{
    *new{|freq=1|
		^Pn(sin(freq * Ptime()).abs, inf)
    }
}

