import re
import glob

path = 'C:\\Users\\Malbolge\\Desktop\\CF\\'
pathdocs = 'C:\\Users\\Malbolge\\Desktop\\CF\\documents\\'

filelist = glob.glob(path + '*.txt')

for file in filelist:

    with open(file, 'r', encoding='utf-8') as f:

        for line in f:
            if re.search(r'RN \d+', line):
                
                docname = int(re.findall(r'\d+', line)[0])
                doc = open(pathdocs + str(docname) + '.txt', 'w', encoding='utf-8')

            if line.startswith('AB') or line.startswith('EX'):
                
                string = line[2:].strip()
                doc.write(re.sub('  ', ' ', string))
                doc.write('\n')
                line = f.readline()

                while not line.startswith('RF'):

                    string = line.strip()
                    doc.write(re.sub('  ', ' ', string))
                    doc.write('\n')
                    line = f.readline()

                doc.close()