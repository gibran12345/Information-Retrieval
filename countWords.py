import re
import glob
from collections import Counter

docspath = 'C:\\Users\\Malbolge\\Desktop\\CF\\documents\\'

filelist = glob.glob(docspath + '*.txt')

stopwords = ['i', 'me', 'my', 'myself', 'we', 'our', 'ours', 'ourselves', 'you', "you're", "you've", "you'll", "you'd", 'your', 'yours', 'yourself', 'yourselves', 'he', 'him', 'his', 'himself', 'she', "she's", 'her', 'hers', 'herself', 'it', "it's", 'its', 'itself', 'they', 'them', 'their', 'theirs', 'themselves', 'what', 'which', 'who', 'whom', 'this', 'that', "that'll", 'these', 'those', 'am', 'is', 'are', 'was', 'were', 'be', 'been', 'being', 'have', 'has', 'had', 'having', 'do', 'does', 'did', 'doing', 'a', 'an', 'the', 'and', 'but', 'if', 'or', 'because', 'as', 'until', 'while', 'of', 'at', 'by', 'for', 'with', 'about', 'against', 'between', 'into', 'through', 'during', 'before', 'after', 'above', 'below', 'to', 'from', 'up', 'down', 'in', 'out', 'on', 'off', 'over', 'under', 'again', 'further', 'then', 'once', 'here', 'there', 'when', 'where', 'why', 'how', 'all', 'any', 'both', 'each', 'few', 'more', 'most', 'other', 'some', 'such', 'no', 'nor', 'not', 'only', 'own', 'same', 'so', 'than', 'too', 'very', 's', 't', 'can', 'will', 'just', 'don', "don't", 'should', "should've", 'now', 'd', 'll', 'm', 'o', 're', 've', 'y', 'ain', 'aren', "aren't", 'couldn', "couldn't", 'didn', "didn't", 'doesn', "doesn't", 'hadn', "hadn't", 'hasn', "hasn't", 'haven', "haven't", 'isn', "isn't", 'ma', 'mightn', "mightn't", 'mustn', "mustn't", 'needn', "needn't", 'shan', "shan't", 'shouldn', "shouldn't", 'wasn', "wasn't", 'weren', "weren't", 'won', "won't", 'wouldn', "wouldn't"]

words = Counter()

for doc in filelist:

    with open(doc, 'r', encoding = 'utf-8') as f:

        for line in f:
            
            line = re.sub('(\: |\:|\,|\"|\(|\))', '', line)
            wordslist = [word for word in re.split(r' ', line) if word]

            for word in wordslist:
                if word and word not in stopwords:
                    words[word] += 1

string = 'C:\\Users\\Malbolge\\Desktop\\CF\\'

with open(string + 'result.txt', 'w', encoding='utf-8') as result:

    for word in words.most_common(100):
        result.write(word[0] + ': ' + str(word[1]) + '\n')

querypath = 'C:\\Users\\Malbolge\\Desktop\\CF\\queries\\'
querywords = Counter()

filelist = glob.glob(querypath + '*.txt')

for queryfile in filelist:
    with open(queryfile, 'r', encoding='utf-8') as f:

        line = f.readline()
        wordlist = re.split(r' ', line)

        for word in wordlist:
            if word and word not in stopwords:
                querywords[word] += 1

with open(string + 'qresult.txt', 'w', encoding = 'utf-8') as result:

    for word in querywords.most_common(100):
        result.write(word[0] + ': ' + str(word[1]) + '\n')