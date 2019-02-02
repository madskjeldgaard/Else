Pdeff : Pdef {
    *new{|pdefname, copyfrom ... args|
        ^Pdef(pdefname,
            Pbindf(
                Pdef(copyfrom),
                *args
            )
        );
    }
}

