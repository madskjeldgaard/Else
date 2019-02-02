Pspeed : Pattern{
    *new{|pat, speed=1|

		^Pchain(pat, (stretch: speed.reciprocal))
    }
}
