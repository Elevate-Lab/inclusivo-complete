import requests
from .models import Country, State, City

json = requests.get('https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master/countries%2Bstates%2Bcities.json')
jsonData = json.json()

for country in jsonData:
    countryName = country["name"]
    countryObject = Country(name = countryName) 
    countryObject.save()

    states = country["states"]
    for state in states:
        stateName = state["name"]
        stateObject = State(name = stateName, country = countryObject) 
        stateObject.save()

        cities = state["cities"]
        for city in cities:
            cityName = city["name"]
            cityObject = City(name = cityName, state = stateObject)
            cityObject.save()