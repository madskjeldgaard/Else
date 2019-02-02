Pwaves : Pattern{

	*new{|eventPatterns, waveLength=1, overWrite=true ... params|
		var sineFunc = {|eventIndex, paramIndex, waveLength|
			var phase = eventIndex.linlin(0,eventPatterns.size, 0.0, 2pi);
			var sine = Psine(( 1+paramIndex )*( 1+eventIndex )*waveLength, phase);

			sine.linlin(-1.0,1.0,0.0,1.0)
		};

		^Pweave(eventPatterns, sineFunc, waveLength, overWrite, *params)
	}

}
