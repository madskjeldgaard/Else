Pwaves : Pattern{

	*new{|eventPatterns, waveLength=1, overWrite=true ... params|
		var wavePats;

		wavePats = eventPatterns.collect{|item, eventIndex|
			var newParams;

			eventIndex = eventIndex+1;

			newParams = params.collect{|param, paramIndex|
				var paramSine = Psine(( 1+paramIndex )*eventIndex*waveLength);
				var paramValue = Pkey(param.asSymbol) * paramSine;

				overWrite.if{paramValue = paramSine};

				[param.asSymbol, paramValue] 
			}.flatten;

			Pbindf(item, *newParams)
		};

		^Ppar(wavePats);
	}

}
