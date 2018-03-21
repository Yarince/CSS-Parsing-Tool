grammar ICSS;

CHAR : [a-z] ;     // match lower-case identifiers
HEXCHAR : [a-f] | [A-F] ;     // match lower-case identifiers
STRING : ([a-z]|[A-Z])+;     // match lower-case identifiers
DIGITS : [0-9] [0-9]*;
INT : [0-9];
WS : [ \t\r\n]+ -> skip ;   // skip spaces, tabs, newlines

/*
    1 char gaat niet
    color wordt herkend als nieuwe id tag....
*/

stylesheet
    : block *
    EOF
    ;

block
    :  selectoren (WS?) BRACKETS blockContent BRACKETS
    ;

blockContent
    : (row | (row ';')+ row ';'? )?
    ;

row
    : styleAttribute WS? ':' property
    ;

styleAttribute
    : 'color'
    | 'background-color'
    | 'width'
    | 'height'
    ;

selectoren
    : ('#'|'.')? STRING
    ;

property
    :  WS? value WS?
    ;

text
    :STRING
    ;

value
    : '$' STRING // TODO VARIABLE
    | DIGITS ('px'|'%')
    | color
    ;

color
    :  '#' (HEXCHAR|INT)//(HEXCHAR|INT)(HEXCHAR|INT)(HEXCHAR|INT)(HEXCHAR|INT)(HEXCHAR|INT)
//    @init { int N = 0; } // TODO Figure out
//    :  ((CHAR|DIGIT) { N++; } )+ { N <= 6 }
    ;

LINK
    : 'a'
    | 'A'
    ;

PARAGRAPH
    : 'p'
    | 'P'
    ;

DIV
    : 'div'
    | 'DIV'
    ;

BRACKETS
    : '{'
    | '}'
    ;