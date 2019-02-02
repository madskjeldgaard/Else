Pwaves : Pattern{

	*new{|eventPatterns, waveLength=1, overWrite=true ... params|
		var sineFunc = {|eventIndex, paramIndex, waveLength|
			Psine(( 1+paramIndex )*( 1+eventIndex )*waveLength, eventIndex.linlin(0,eventPatterns.size, 0.0, 2pi)).linlin(-1.0,1.0,0.0,1.0)
		};

		^Pweave(eventPatterns, sineFunc, waveLength, overWrite, *params)
	}

}
