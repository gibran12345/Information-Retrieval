import re
import glob

qpath = 'C:\\Users\\Malbolge\\Desktop\\CF\\queries\\'
qdocspath = 'C:\\Users\\Malbolge\\Desktop\\CF\\queriedocs\\'

qpathlist = glob.glob(qpath + '*.txt')
qdocspathlist = glob.glob(qdocspath + '*.txt')

precision = 0.0

for relevantDocument, returnedDocument in zip(qpathlist, qdocspathlist):

    with open(relevantDocument) as query:
        with open(returnedDocument) as querydoc:

            rdocnumbers = query.readline()
            rdocnumbers = query.readline()
            adocnubmers = querydoc.readline()
            
            S = []
            N = [int(number) for number in re.split(r' ', rdocnumbers) if number] # Relevant Documents
            R = [int(number) for number in re.split(r' ', adocnubmers) if number] # Returned Documents (top 100)

            avep = 0.0
            for i, number in enumerate(R, 1):
                if number in N:
                    S.append(number)
                    avep += len(S) / i

            precision += avep / len(N)

print(precision)
print(precision / 100)