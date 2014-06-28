#! /bin/sed -f

#
# File: manual.frodo
#
# Generate programmer documentation for the directives from the Javadocs in
# LOTR.java.
#

# Edit all the lines between the Directive line and the end of javadoc
/^     \* Directive:/,/^     \*\// {
    #
    /^     \* Directive:/ s/^     \* //

    # remove the closing */ and add an extra blank line
    /^     \*\// {
        s/.*//
        a
    }

    # fix up the return and parameter lines, and remove the leading star
    /^     \* @return/ s/^     \* @return/    Result:/
    /^     \* @param/ s/^     \* @param/    Parameter:/
    /^     \*/ s/^     \*/   /

    # remove the <code> and </code> tags
    s/<code>//g
    s/<\/code>//g

    # skip the rest and read the next line
    b
}

# delete all other lines
d
