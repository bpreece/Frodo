#! /bin/awk -f

#
# File: manual.awk
#
# Generate programmer documentation for the directives from the Javadocs in
# LOTR.java.
#

BEGIN {
    skip = 1  # state variable: zero to print, one to skip
}

# Find the next directive
/^     \* Directive:/ && skip == 1 {
    print substr($0,8,1000)
    # now we start editing, not skipping
    skip = 0
}

# If we're editing, handle return value
/^     \* @return/ && skip == 0 {
    print "    Result: " substr($0,16,1000)
}

# If we're editing, handle parameters
/^     \* @param/ && skip == 0 {
    print "    Parameter: " substr($0,15,1000)
}

/^     \*/ && ! /^     \*\// && ! /^     \* Directive:/ && ! /^     \* @param/ && ! /^     \* @return/ && skip == 0 {
    print "    " substr($0,8,1000)
}

# remove the closing */ and add an extra blank line
/^     \*\// && skip == 0 {
    print ""
    print ""
    # now we start skipping again
    skip = 1
}
