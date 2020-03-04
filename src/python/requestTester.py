import requests
import json
from html.parser import HTMLParser

# leagueVal = 'uefa.euroq'
leagueVal = 'uefa.euro'
targetDate = '20200612'

class GameRecord():
    def __init__(self):
        self._homeTeam
        self._awayTeam
        self._dateTime
        self._gameId

    def setHomeTeam(self, team, flag):
        self._homeTeam = {'name':team, 'flag':flag}

    def setAwayTeam(self, team, flag):
        self._awayTeam = {'name':team, 'flag':flag}

    def setDateTime(self, dateTime):
        self._dateTime = {'time':dateTime}

    def setGameId(self, id):
        self._gameId = {'id':id}
    

class TableParser(HTMLParser):
    def __init__(self, f):
        super().__init__()
        self._f = f
        self._indentCount = 0
        self._log = False
        self._isTableBody = False

    def handle_starttag(self, tag, attrs):
        if tag == 'table':
            self._log = True
        if tag == 'tbody':
            self._isTableBody = True
        if tag != 'img':
            self._indentCount += 1
        
        self._output('%s %s' % (tag, attrs))
        # self._output(self.get_starttag_text())

    def handle_endtag(self, tag):
        if tag == 'table':
            self._log = False
        if tag == 'tbody':
            self._isTableBody = False
        self._output(tag)
        self._indentCount -= 1
        # self._output(self.get_endtag_text())

    def handle_data(self, data):
        self._indentCount += 1
        self._output(data)
        self._indentCount -= 1

    def handle_startendtag(self, tag, attrs):
        self._output('%s %s' % (tag, attrs))
        # self._output(self.get_starttag_text())

    def _output(self, outStr):
        if self._log:
            self._f.write('%s%s\n' % ('\t'*self._indentCount, outStr))


def printAllProps(thing):
    f = open("output.txt",'w+')
    content = json.loads(thing.content.decode(thing.encoding))
    for k in content.keys():
        if k != 'altScheduleDropdown':
            if k == 'table':
                f.write(k + '\n')
                parser = TableParser(f)
                parser.feed(content[k])
            else:
                f.write('%s:%s\n'% (k, content[k]))

def constructRequest(date, league):
    return 'https://secure.espn.com/core/soccer/fixtures/_/date/%s/league/%s?table=true&device=desktop&country=ca&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full'%(date, league)

def main():
    # request = 'https://secure.espn.com/core/football/scoreboard/_/league/uefa.euroq?xhr=1&render=true&device=desktop&country=ca&lang=en&region=gb&site=espn&edition-host=espn.co.uk&site-type=full'
    #request for games on a given day
    # request = 'https://secure.espn.com/core/soccer/fixtures/_/date/20200612/league/uefa.euro?table=true&device=desktop&country=ca&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full'
    #request for a particular game
    # request = 'https://secure.espn.com/core/soccer/match?gameId=560344&xhr=1&render=true&device=desktop&country=ca&lang=en&region=us&site=espn&edition-host=espn.com&site-type=full'
    request = constructRequest(targetDate, leagueVal)
    response = requests.get(request)
    printAllProps(response)

if __name__ == '__main__':
    main()