Ppal : ListPattern{

	*new{|list, repeats=1, offset=0|

		^Pseq.new(list ++ list.reverse, repeats, offset);
	}


}
