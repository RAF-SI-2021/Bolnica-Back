# RAF SI - BOLNICA - BACKEND

## How to run using docker compose

- Download Docker
- Go in the directory where the docker-compose.yaml file is located
- From terminal run command `docker-compose up`
- To shut down the services press Ctrl + C or open another tab in terminal and run command `docker-compose down`

### Napomena
Iz nekog razloga se ne pravi baza user tako da ce morati manuelno da se napravi tako sto se udje u localhost:8080 i da se doda baza rucno i onda ce proraditi.
Takodje kada budete razvijali dalje aplikacije potrebno ce biti da se odradi `maven clean install` svaki put kad zelite da pokrenete aplikacije sa izmenama koje ste napravili kako bi se ponovo napravio jar fajl koji se koristi za docker image.
