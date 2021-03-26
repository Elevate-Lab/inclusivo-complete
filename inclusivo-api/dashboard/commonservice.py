

def getSearchType(searchType):
    if searchType is None or searchType == "":
        return "CONTAINS"
    switcher = {
        "CONTAINS": searchType,
        "EQUALS": searchType,
        "IN": searchType,
        "BETWEEN": searchType,
        "GREATERTHAN": searchType,
        "LESSTHAN": searchType,
    }
    return switcher.get(searchType.upper(), "CONTAINS")


def getSearchQueryField(searchType, fieldDatatype, dbField, searchText):
    query = ""
    if fieldDatatype == "string":
        if searchType == "CONTAINS":
            query = dbField + " ILIKE " + "'%%" + searchText + "%%'",
        elif searchType == "EQUALS":
            query = dbField + " = " + "'" + searchText + "'"
        elif searchType == "IN":
            query = dbField + " IN ('" + searchText.replace("#@#", "','") + "')"
        elif searchType == "BETWEEN":
            parts = searchText.split("#@#")
            query = dbField + " BETWEEN '" + parts[0] + "' and '" + parts[1] + "'"
    elif fieldDatatype == "int":
        if searchType == "CONTAINS" or searchType == "EQUALS":
            query = dbField + " = " + searchText
        elif searchType == "IN":
            searchText = searchText.replace("#@#", ",")
            query = dbField + " IN (" + searchText + ")"
        elif searchType == "GREATERTHAN":
            query = dbField + " > " + searchText
        elif searchType == "LESSTHAN":
            query = dbField + " < " + searchText
        elif searchType == "BETWEEN":
            parts = searchText.split("#@#")
            query = dbField + " BETWEEN " + parts[0] + " and " + parts[1]
    return "".join(query)