### Neighborhoods
CENTER: The current state of the center cell (in plane 0).
CENTER': The previous state of the center cell (in plane 1, used in second-order dynamics).
NORTH, SOUTH, WEST, EAST, N.WEST, N.EAST, S.WEST, S.EAST: The current states of the neighboring cells (in plane 0).
CENTERS is defined as CENTER+ 2 x CENTER•, and takes on the values O , 1 , 2 , and 3 .

Of course, existing neighbor words can be renamed at any time to suit one's
taste. For example, it is always legitimate to write
                UNDERLAY
CENTER' ;
and then refer to UNDERLAY rather than CENTER• in the body of a rule.

The minor assignment &/CENTERS connects our last two probes to the
other half of the machine, giving us a 1 x 1 window on the other two planes;
these extra neighbors are called
&CENTER
&CENTER'
or, collectively, &CENTERS

The declaration re/PHASEcSon nects the two extra probes to two of these
pseudo-neighbors, namely
&PHASE
&PHASE'
also known to CAM Forth by the collective name &PHASES.

Finally, the minor assignment &/HV selects two pseudo-neighbors called
&HORZ
&VERT
(horizontal phase and vertical phase), which provide some space-dependent
information; in particular, they allow one to make up rules that follow a
"striped" or a "checkerboard" pattern, and they support the partitioning
technique discussed in Chapter 12. They will be explained in more detail in
Section 11.1 below. The collective name for this pair of pseudo-neighbors is
.tHV (=&HORZ+2x&VERT).

We shall use the term noisy neighbor
for any quantity that can play the role of a random variable in the definition
of a rule.

### Composition
A general and quite powerful method of extending the range of behavior
that can be explored with a cellular automata machine is by rule composition.
62 Chapter 7. Neighbors and neighborhoods
That is, by doing one step with a given rule a, one with rule b, and so on
through an assigned sequence, we can effectively construct a "super-rule,,
with properties that are beyond the reach of the individual components.