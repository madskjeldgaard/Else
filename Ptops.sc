 Ptops : Pattern{

	*new{|eventPatterns, waveLength=1, overWrite=true ... params|
		var sineFunc = {|eventIndex, paramIndex, waveLength|
			Psine(( 1+paramIndex )*( 1+eventIndex )*waveLength)
		};

		^Pweave(eventPatterns, sineFunc, waveLength, overWrite, *params)
	}

}
