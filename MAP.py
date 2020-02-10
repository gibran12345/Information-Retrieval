import re
import glob

qpath = 'C:\\Users\\Malbolge\\Desktop\\CF\\queries\\'
qdocspath = 'C:\\Users\\Malbolge\\Desktop\\CF\\queriedocs\\'

N = set() # Documntos relevantes
R = set() # Documentos retornados (top 100)

qpathlist = glob.glob(qpath + '*.txt')
qdocspathlist = glob.glob(qdocspath + '*.txt')

precision = 0.0

for relevantDocument, returnedDocument in zip(qpathlist, qdocspathlist):

    with open(relevantDocument) as query:
        with open(returnedDocument) as querydoc:

            rdocnumbers = query.readline()
            rdocnumbers = query.readline()
            adocnubmers = querydoc.readline()
            
            N = {int(number) for number in re.split(r' ', rdocnumbers) if number}
            R = {int(number) for number in re.split(r' ', adocnubmers) if number}

            precision += len(N & R) / len(R)

print(precision)
print(precision / 100)