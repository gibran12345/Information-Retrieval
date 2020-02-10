import glob
import re

path = 'C:\\Users\\Malbolge\\Desktop\\CF\\other\\'
pathq = 'C:\\Users\\Malbolge\\Desktop\\CF\\queries\\'

with open(path + 'cfquery.txt', 'r', encoding = 'utf-8') as queries:

    for line in queries:

        qnumber = int(re.findall(r'\d+', line)[0])

        doc = open(pathq + str(qnumber) + '.txt', 'w', encoding = 'utf-8')
        
        line = queries.readline()
        if line.startswith('QU'):
            
            string = line[2:].strip()
            doc.write(re.sub('  ', ' ', re.sub('(\(|\)|\?|\/)', '', string)))
            line = queries.readline()

            while not line.startswith('NR'):
                
                string = line.strip()
                doc.write(' ' + re.sub('  ', ' ', re.sub('(\(|\)|\?|\/)', '', string)))
                line = queries.readline()

            line = queries.readline()
            doc.write('\n')
            if line.startswith('RD'):
                
                while line.startswith(' ') or line.startswith('RD'):
                    
                    if line.startswith('RD'):
                        line = line[2:].strip()

                    docs = [i for i in re.split(' ', line.strip()) if i]

                    if not docs:
                        break

                    flag = False
                    for number, docnumber in enumerate(docs, 0):
                        if number % 2 == 0:
                            doc.write(docnumber + ' ')

                    line = queries.readline()

                doc.close()