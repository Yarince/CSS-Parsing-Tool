grammar ICSS;

// Lexer rules

COLOR :  '#' HEXCHAR HEXCHAR HEXCHAR HEXCHAR HEXCHAR HEXCHAR ;
BRACKET : '{' | '}' ;

COLOR_PROP : 'color' ;
BACKGROUND_COLOR_PROP : 'background-color' ;
WIDTH_PROP : 'width' ;
HEIGHT_PROP : 'height' ;
ID : '#' STRING ;
CLASS : '.' STRING ;
PIXEL : DIGITS 'px';
PERCENTAGE : DIGITS '%';

WS : [ \t\r\n]+ -> skip ;           // skip spaces, tabs, newlines
STRING : CHAR+ ;                    // match lower-case identifiers
VARIABLE : '$'[a-zA-Z_]+ ;          // match lower-case identifiers
DIGITS : INT+ ;                   // match digits
fragment INT : [0-9] ;                   // match digits
fragment HEXCHAR : [0-9a-fA-F] ;    // match hexadecimal chars
fragment CHAR : [a-zA-Z] ;          // match char

// Parser rules

stylesheet
    : (variableInit
    | block
    | switchcase)*
    EOF
    ;

/*
    Variable
*/

variableInit
    : 'let' variable 'is' (value|DIGITS) ';'
    ;

variable
    : VARIABLE
    ;

switchcase
    : selectoren 'switch' VARIABLE  caseOption+ defaultOptioin?
    ;

caseOption
    : 'case'  (value | DIGITS) BRACKET blockContent BRACKET
    ;

defaultOptioin
    : 'default' BRACKET blockContent BRACKET
    ;

/*
    Sytle block
*/
block
    :  selectoren BRACKET blockContent BRACKET
    ;

blockContent
    : (row ';'?) | ((row ';')+ row ';'? )?
    ;

// set value and do optional calculation
row
    : styleAttribute ':' value value_calc*
    ;

styleAttribute
    : COLOR_PROP
    | BACKGROUND_COLOR_PROP
    | WIDTH_PROP
    | HEIGHT_PROP
    ;

selectoren
    : ID
    | CLASS
    | STRING
    ;

value
    : variable
    | PERCENTAGE
    | PIXEL
    | COLOR
    ;

value_calc
    : ('+' | '*' | '-' ) (DIGITS | value)?
    ;
