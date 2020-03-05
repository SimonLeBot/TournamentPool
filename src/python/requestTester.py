import requests
import json
from html.parser import HTMLParser

# leagueVal = 'uefa.euroq'
leagueVal = 'uefa.euro'
targetDate = '20200612'

class GameRecord():

    _nameKey = 'name'
    _flagKey = 'flag'
    _timeKey = 'time'
    _idKey = 'id'

    def __init__(self):
        self._homeTeam = {}
        self._awayTeam = {}
        self._dateTime = {}
        self._gameId = {}

    def setHomeTeamName(self, team):
        self._homeTeam = self._setTeamName(self._homeTeam, team)

    def setHomeTeamFlag(self, flag):
        self._homeTeam = self._setTeamFlag(self._homeTeam, flag)

    def setAwayTeamName(self, team):
        self._awayTeam = self._setTeamName(self._awayTeam, team)

    def setAwayTeamFlag(self, flag):
        self._awayTeam = self._setTeamFlag(self._awayTeam, flag)

    def setDateTime(self, dateTime):
        self._dateTime = {self._timeKey:dateTime}

    def setGameId(self, id):
        self._gameId = {self._idKey:id}

    def hasHomeTeam(self):
        return self._isTeamComplete(self._homeTeam)

    def hasAwayTeam(self):
        return self._isTeamComplete(self._awayTeam)

    def hasDateTime(self):
        return self._timeKey in self._dateTime

    def hasGameId(self):
        return self._idKey in self._gameId

    def isComplete(self):
        return self.hasHomeTeam() and self.hasAwayTeam() and self.hasDateTime() and self.hasGameId()

    def __str__(self):
        return 'Home:%s Away:%s Date:%s Id:%s'%(self._homeTeam, self._awayTeam, self._dateTime, self._gameId)

    def _isTeamComplete(self, team):
        return self._nameKey in team and self._flagKey in team

    def _setTeamName(self, var, team):
        var['name'] = team
        return var

    def _setTeamFlag(self, var, flag):
        var['flag'] = flag
        return var

class TableParser(HTMLParser):
    def __init__(self, f):
        super().__init__()
        self._f = f
        self._indentCount = 0
        self._log = False
        self._foundTeamNameClass = False
        self._foundTeamLogoClass = False
        self._foundDate = False
        self._games = []
        self._construct = []

    def handle_starttag(self, tag, attrs):
        if tag == 'table':
            self._log = True
        if tag != 'img':
            self._indentCount += 1
        if tag == 'tr':
            self._construct = GameRecord()
        if self._construct:
            attrs = self._convertToDict(attrs)
            if 'class' in attrs and attrs['class'] == 'team-name':
                self._foundTeamNameClass = True
            if self._foundTeamNameClass and tag == 'abbr' and 'title' in attrs:
                if self._construct.hasHomeTeam():
                    self._construct.setAwayTeamName(attrs['title'])
                else:
                    self._construct.setHomeTeamName(attrs['title'])
                self._foundTeamNameClass = False
            if 'class' in attrs and attrs['class'] == 'team-logo':
                self._foundTeamLogoClass = True
            if self._foundTeamLogoClass and tag == 'img' and 'src' in attrs:
                if self._construct.hasHomeTeam():
                    self._construct.setAwayTeamFlag(attrs['src'])
                else:
                    self._construct.setHomeTeamFlag(attrs['src'])
                self._foundTeamFlagClass = False
            if 'data-date' in attrs:
                self._construct.setDateTime(attrs['data-date'])
                self._foundDate = True
            if self._foundDate and tag == 'a' and 'href' in attrs:
                self._construct.setGameId(attrs['href'].split('=')[1])
                self._foundDate = False

        
        self._output('%s %s' % (tag, attrs))
        # self._output(self.get_starttag_text())

    def handle_endtag(self, tag):
        if tag == 'table':
            self._log = False
        if tag == 'tr':
            if self._construct.isComplete():
                self._games.append(self._construct)
                print('New record: %s'%self._construct) 
            else:
                print('WARN: incomplete game record %s' % self._construct)
            self._construct = []

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

    def _contains(self, items, desired):
        for ii in items:
            if desired in ii:
                return True
        return False

    def _convertToDict(self, attrs):
        newAttrs = {}
        for attr in attrs:
            newAttrs[attr[0]] = attr[1]
        return newAttrs

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