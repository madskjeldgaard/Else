+ Pdef {
	*allPhrases{|phrasePrefix='phr_'|
		^this.all.keys.reject{|pdefname|
			var sliceTo = phrasePrefix.asString.size-1;

			pdefname.asString[0..sliceTo] != phrasePrefix.asString;

		}.asArray
	}

}
//
// p = (
// 	// Get all phrases as an array
// 	phrases: { |self, phrasePrefix = 'phr_'|
// 		Pdef.all.keys.reject{|pdefname|
// 			var sliceTo = phrasePrefix.asString.size-1;
//
// 			pdefname.asString[0..sliceTo] != phrasePrefix.asString;
//
// 		}.asArray
// 	},
//
// 	// Get all recursive phrases as an array
// 	recursivePhrases: {|self, phrasePrefix='rephr_'|
// 		self.phrases(phrasePrefix);
// 	},
//
// 	// Get all multichannel phrases as an array
// 	multichanPhrases: {|self, phrasePrefix='mulphr_'|
// 		self.phrases(phrasePrefix);
// 	}
// );
