## Kuidas käivitada

Dockeri kaudu:

~~~bash
docker-compose up --build
~~~

## Projekti kulgemine

### Esmane lahendus

Keskkonna seadistamine ja esmane lennu valimine 1.5h

> Valisin frontend-raamistikuks React'i, kuna olen sellega varem töötanud. Tailwind CSS oli uus, aga see osutus üsna mugavaks, v.a paaris kohas, kus nt salongi vahekäigu väljakuvamiseks ei olnud võimalik keset Tailwind grid'i teha suurema vahe

Istekohtade soovitamine koos 2 inimesega 4.5h

> Praeguse TLL-HEL Finnair'i ATR 72 lennuki istmete põhjal tegin algoritmi, mis otsib kõrvuti olevaid istekohti, kuid siis ei olnud lahendus universaalne, nt juhul kui lennukil on kõrvuti istekohad AC, mitte AB

Istekohtade soovitamise filtrite ümbertegemine 4h

> Ei olnud siis veel kindel, millisel kujul andmeid hoida ja millisele kujule need viia

Istekohtade klassid 4h

> Kuigi klasse kasutasin ainult ühes kohas koodis, tahtsin neid ikkagi enumitena hoida, kui magic stringina.

### Lennuandmetega katsetamine

> Siis arvasin, et olen projekti enam-vähem "ära teinud". Tunnistan, et oleksin pidanud veel läbi mõtlema, kuidas olemasolevat lahendust teha silmapaistvamaks. See-eest sain teada, millised API-d lennuandmete jaoks on üldse saadaval ja milliseid andmeid need annavad

Lennujaamade lendude otsing 4h

> Alguses mõtlesin laadida sisse kõik lennujaamad, kuid sain hiljem aru, et mul ei ole kõiki vaja

Lennuki tüübi otsimine lennu numbrit järgi (algus) 2h

> Arvasin siis, et istmekaardi leidmiseks piisab, kui tean lennuki mudelit. Küll aga sain alles pärast aru, et ühel lennuki tüübil võib olla mitu istmekaarti. Seega ma ei oleks pidanud sellele aega kulutama, kui andmeid, mida vajan, ei ole isegi olemas

Staatilise ja live andmete eraldamine 5h

> Korraks mõtlesin ühildada staatilise ja live ehk API-andmete struktuuri, kuid see oleks tähendanud andmete viimist kujule, mis ei oleks paindlik ja mis sõltuks API-st

Istekohtade lugemine JSON'ist 2h

> Ei olnud kõige loetavam JSON, kuna raske oli eristada istmeid, mida meelega jäeti vahele, vs neid, mida võis "kogemata" vahele jätta ja vigaste andmetena esitada

Dünaamiline istekohtade kuvamine 3.5h

> Nagu eelnevalt mainitud, võib samal lennuki tüübil olla mitu istmekaarti, mistõttu selle dünaamilisemaks tegemine tähendaks eelnevat teadmist, millised istmed on konkreetselt sellel lennukil. Paraku ei leidnud API-t, kus oleksin saanud seda neid lugeda, ilma et peaks maksma API kasutuse eest või kasutama mitteametliku API-d. Sain siis aru, et peaksin keskenduma baaslahendusele, mis vastaks ülesande ootustele

### Ülesande ümbermõtestamine

> Hakkasin mingis mõttes nullist uuesti pihta. Vaatasin ülesandele veelkord otsa ja proovisin aru saada, mida täpselt tahetakse. Kuna ei olnud varem kokku puutunud proovitööga, kus on antud nii palju vaba ruumi, siis ei osanud isegi kohe mõelda, mida sellega peale hakata. Kui oleksin alguses rohkem selle peale mõelnud, oleksin vähem koodi ümber kirjutanud

Esimene etapp ja teise etappi andmete ümberkirjutamine 3.5h

> Hakkasin katsetama andmete üleviimist YAML'isse, kuna minu arust on see loetavam kui JSON

Veel ümberkirjutamist ja teise etappi andmete kuvamine 6h

> Alles siis hakkas andmete struktuur sarnanema sellele, mis praegu on, kuid istmetele ei olnud veel märgitud positsioone. Sain aru, et tuleb leida parem lahendus, millega normaalselt kuvada istmeid kasutajaliideses (mis ei vajaks eraldi loogikat frontendi pool) ja leida parem algoritm kõrvuti olevate istmete tuvastamiseks

Teise etappi universaalsemaks tegemine frontendi jaoks 6h

> Sain valmis lahenduse, mis töötab igasuguste istmekaartide jaoks

Frontendi normaalsemaks tegemine 1.5h

> Kasutajaliidese ja -kogemuse pani kokku AI

Istekohtade klasside filter ja rakenduse käivitamise dokumentatsioon 1.5h

> Kuna oli veel aega, ja kuna istmete klassid olid andmetes juba olemas, lisasin nendele filtri juurde

Dockeri konteinerisse paigutamine 3.5h

> Dockerfile'id olid AI genereeritud, mida käsitsi mudisin