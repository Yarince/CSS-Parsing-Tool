# CSS-Parsing-Tool

Je gaat in deze opdracht dus een eigen CSS dialect maken: ICSS-18-2 (ICA-CSS). Een informele beschrijving
van deze taal is te vinden in appendix A. Lees deze beschrijving goed!
Je bouwt in deze opdracht verder aan een interactieve Java applicatie: De ICSS tool. Deze tool is een interactieve
compiler. Je kunt er interactief ICSS in bewerken en deze stapsgewijs compileren naar CSS. Deze CSS
kun je vervolgens exporteren. Het raamwerk voor de ICSS tool krijg je als startcode aangeleverd. De GUI is al
gemaakt en alle onderdelen zijn in minimale vorm aanwezig. De opdracht bestaat uit het volledig maken van
de tool door middel van serie deelopdrachten.

## ICSS-18-2 : Informele Specificatie

ICSS is een opmaaktaal vergelijkbaar met Cascading Stylesheets (CSS). Het heeft niet alle mogelijkheden van
CSS, maar tegelijkertijd heeft het ook een aantal features die CSS niet heeft.
Dit document beschrijft op informele wijze mogelijkheden van de ICSS-18-2 versie van ICSS.

###Eenvoudige opmaak

ICSS gebruikt net als CSS regels om de opmaak van HTML elementen aan te geven. Een stylesheet bestaat
uit een aantal regels die na elkaar worden toegepast op een HTML document.
Regels hebben de vorm `<selector> { <declaraties> }`. Hierin is de selector ofwel een specifiek type tag
geselecteerd kan worden, ofwel een element met een unieke id, ofwel elementen van een bepaalde class. Elementen
met een uniek id worden aangegeven door identifier beginnend met een hekje (#) en elementen in een
klass worden aangegeven door de klassenamen voorafgegaan door een punt (.). Declaraties zijn naam/waarde
paren van de vorm `<attribuutnaam>: <waarde>;`. Sommige waardes kunnen ook een eenheid bevatten zoals
px of %.

Hier volgen een aantal voorbeelden van eenvoudige ICSS regels:
```css
a {
color: #ff0000;
background-color: #eeeeee;
}
#menu {
width: 100%;
height: 50px;
}
.active {
color: #00ffff;
}
```
####Beperkingen

ICSS is beperkter dan CSS. Dit zijn de beperkingen:
* Selectoren zijn allemaal lowercase.
* Selectoren selecteren maar op één ding tegelijk. Combinaties zoals a.active zijn niet toegestaan.
* Selectoren voor het selecteren van kinderen uit CSS zoals div > a zijn niet toegestaan
* Alleen de stijlattributen color,background-color,width en height zijn toegestaan.
* Voor kleuren (color en background-color) moet de waarde als een hexadecimale waarde van zes tekens
opgegeven worden. (Bijvoorbeeld: #00ff00)
* Voor groottes mag of een waarde in pixels `(bijvoorbeeld: 100px)` of een percentage `(bijvoorbeeld 50%)`
gespecifieerd worden.

###Constantes

Een feature die CSS niet heeft, maar ICSS wel is de definitie van constante waardes. In ICSS kun je expressies
een naam geven en dan op meerdere plaatsen waar je anders een waarde zou invullen die naam gebruiken.

Een definitie van een constante ziet er als volgt uit: `let $MYVAR is 100px;`

Het gebruik ervan is dan: `width: $MYVAR;`

Je kunt natuurlijk ook constantes uitdrukken op basis van een andere constante: `let $TEXTCOLOR is $BGCOLOR;`

Alle constantes moeten bovenaan het document gedefinieerd worden en hebben een globale scope. Ze zijn
volledig uppercase geschreven. Je mag constantes gebruiken voordat ze gedefinieerd zijn.

###Berekende waardes

Een andere uitbreiding in ICSS is de mogelijkheid om eenvoudige berekeningen te doen met waardes. In ICSS
mag je pixelwaardes en percentages optellen en aftrekken en vermenigvuldigen. Dit mag zowel in stijldeclaraties
van attributen als in de definifitie van constanten.

Bijvoorbeeld:
```
div {
width: 50px + 50px - 2px;
}
#menu {
height: 20px + $MYHEIGHT * 2;
}
```
of
```
let $MENUSIZE is $HEADERSIZE - 20%;
```
Je mag alleen pixelwaardes bij pixelwaardes optellen en percentages bij percentages. Kleuren kun je niet
optellen. Vermenigvuldigen gaat met scalaire waarden zoals 42 of 3. Die kun je onderling ook optellen of
vermenigvuldigen. Je mag deze waarden natuurlijk niet gebruiken als waarde van attributen.

###Selectie van stijlregels

Je kunt in ICSS een keuze maken tussen verschillende varianten van regels. Hiervoor is er switch/case constructie.
Er wordt op basis van de waarde van een expressie gekeken welke variant van een regel gekozen
wordt.

Dit ziet er dan bijvoorbeeld zo uit:
```
let $BROWSER is 42;
h2 switch $BROWSER
case 42 {
color: #ff00ff;
}
case 34 {
color: #ff0000;
}
default {
color: #00ff00;
}
```
