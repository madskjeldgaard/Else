Pweave : Pattern{

	*new{|eventPatterns, weaveFunc, weaveSpeed=1, overWrite=true ... params|
		var weavePats = eventPatterns.collect{|eventPat, eventIndex|
			var newParams = params.collect{|param, paramIndex|
				var paramFunc = weaveFunc.value(eventIndex, paramIndex, weaveSpeed);

				var paramValue = Pkey(param.asSymbol) * paramFunc;

				overWrite.if{paramValue = paramFunc};

				[param.asSymbol, paramValue] 
			}.flatten;

			Pbindf(eventPat, *newParams)
		};

		^Ppar(weavePats);
	}

}
