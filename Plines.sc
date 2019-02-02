/*

Line patterns

*/

Pexpodec : ListPattern {
    *new{|steps=10, repeats=1|
        var revxline = Array.fill(steps, {|i| i.linexp(0,steps-1,0.00001,1.0)}).reverse;

        ^Pseq(revxline, repeats);
    }
}

Prexpodec : ListPattern {
    *new{|steps=10, repeats=1|
        var xline = Array.fill(steps, {|i| i.linexp(0,steps-1,0.00001,1.0)});

        ^Pseq(xline, repeats);
    }
}
