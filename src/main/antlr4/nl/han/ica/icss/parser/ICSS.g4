grammar ICSS;

fragment CHAR : [a-z] ;     // match lower-case identifiers
WS : [ \t\r\n]+ -> skip ;   // skip spaces, tabs, newlines

stylesheet: WS + EOF;

selector :  'a' + WS + Brackets + EOF; //(A)?|p|div

fragment A
    : 'a'
    | 'A'
    ;

fragment p
    : 'p'
    | 'P'
    ;

fragment div
    : 'div'
    | 'DIV'
    ;

fragment id
    : '#'
    ;

fragment Brackets
    : '{'
    | '}'
    ;
