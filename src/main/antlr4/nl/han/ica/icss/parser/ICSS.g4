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
ADDITION : '+' ;
MULTIPLICATION : '*' ;
SUBSTRACTION : '-' ;

WS : [ \t\r\n]+ -> skip ;           // skip spaces, tabs, newlines
VARIABLE : '$'[a-zA-Z_]+ ;          // match lower-case identifiers
TAG : STRING ;
DIGITS : INT+ ;                   // match digits

fragment STRING : CHAR+ ;                    // match lower-case identifiers
fragment INT : [0-9] ;                   // match digits
fragment HEXCHAR : [0-9a-fA-F] ;    // match hexadecimal chars
fragment CHAR : [a-zA-Z] ;          // match char

// Parser rules

stylesheet
    : ( variableInit
    | block
    | switchCase ) *
    EOF
    ;

/*
    Variable
*/

variableInit
    : 'let' VARIABLE 'is' value ';'
    ;

switchCase
    : selectors 'switch' VARIABLE  caseOption+ defaultOption?
    ;

caseOption
    : 'case'  value BRACKET blockContent BRACKET
    ;

defaultOption
    : 'default' BRACKET blockContent BRACKET
    ;

/*
    Sytle block
*/
block
    :  selectors BRACKET blockContent BRACKET
    ;

blockContent
    : (row ';'?) | ((row ';')+ row ';'? )?
    ;

// set value and do optional calculation
row
    : styleAttribute ':' value valueCalc*
    ;

styleAttribute
    : COLOR_PROP
    | BACKGROUND_COLOR_PROP
    | WIDTH_PROP
    | HEIGHT_PROP
    ;

selectors
    : ID
    | CLASS
    | TAG
    ;

value
    : VARIABLE
    | PERCENTAGE
    | PIXEL
    | COLOR
    | DIGITS
    ;

valueCalc
    : (ADDITION | MULTIPLICATION | SUBSTRACTION ) value?
    ;
