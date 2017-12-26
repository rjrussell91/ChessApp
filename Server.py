import socket, threading
socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = ''
socket.bind((host, 8000))
socket.listen(20)
players = []
lobby = []

# wrapper function read from socket
def readnbytes(socket, n):
    s = ''
    count = 0
    while count < n:
        temp = socket.recv(1)
        s = s + temp
        count += 1
    return s

# wrapper function to write to socket
def sendnbytes(socket, msg, n):
    sent = 0
    while sent < n:
        temp = socket.send(msg[sent:])
        sent += temp

  #thread that handles communication with one of the players
class Player(threading.Thread):
    def __init__(self, con, username):
        threading.Thread.__init__(self)
        self.con = con
        self.game = None
        self.username = username

    # loop to listen for input from client
    def run(self):
        while True:
            msg = readnbytes(self.con, 2)

            # move
            if msg == "MV":
                msg += readnbytes(self.con, 5)
                if self.game.whitePlayer == self:
                    sendnbytes(self.game.blackPlayer.con, msg, 7)
                else:
                    sendnbytes(self.game.whitePlayer.con, msg, 7)
            # pawn promotion
            if msg == "PR":
                msg += readnbytes(self.con, 6)
                if self.game.whitePlayer == self:
                    sendnbytes(self.game.blackPlayer.con, msg, 8)
                else:
                    sendnbytes(self.game.whitePlayer.con, msg, 8)
            # exit game
            if msg == "EX":
                sendnbytes(self.con, "EX\n",3)
                self.con.close()
                if self in lobby:
                    lobby.remove(self)
                # let other player know we disconnected
                if self.game is None:
                    break
                if self.game.whitePlayer == self:
                    try:
                        sendnbytes(self.game.blackPlayer.con, "DC\n", 3)
                    finally:
                        break
                else:
                    try:
                        sendnbytes(self.game.whitePlayer.con, "DC\n", 3)
                    finally:
                        break
                break
            # resign
            if msg == "RN":
                # let other player know we resigned
                if self.game.whitePlayer == self:
                    sendnbytes(self.game.blackPlayer.con, "RN\n", 3)
                else:
                    sendnbytes(self.game.whitePlayer.con, "RN\n", 3)
            # offer draw
            if msg == "OD":
                # ask other player if he agrees to draw
                if self.game.whitePlayer == self:
                    sendnbytes(self.game.blackPlayer.con, "OD\n", 3)
                else:
                    sendnbytes(self.game.whitePlayer.con, "OD\n", 3)
            # declined draw
            if msg == "DD":
                # let other player know you declined
                if self.game.whitePlayer == self:
                    sendnbytes(self.game.blackPlayer.con, "DD\n", 3)
                else:
                    sendnbytes(self.game.whitePlayer.con, "DD\n", 3)
            # accepted draw
            if msg == "AD":
                # let other player know
                if self.game.whitePlayer == self:
                    sendnbytes(self.game.blackPlayer.con, "AD\n", 3)
                else:
                    sendnbytes(self.game.whitePlayer.con, "AD\n", 3)
            #  new game
            if msg == "NG":
                lobby.append(self)
                if len(lobby) == 2:
                    white = lobby[0]
                    black = lobby[1]
                    lobby.pop()
                    lobby.pop()
                    game = ChessGame(white, black)
                    game.start()
            # cancel button
            if msg == "CB":
                lobby.remove(self)

class ChessGame:
    def __init__(self, white, black):
        self.whitePlayer = white
        self.blackPlayer = black

    def start(self):
        self.whitePlayer.game = self
        self.blackPlayer.game = self

        sendnbytes(self.whitePlayer.con, "WH" + self.blackPlayer.username + "\n", len(self.blackPlayer.username)+3)
        sendnbytes(self.blackPlayer.con, "BL" + self.whitePlayer.username + "\n", len(self.whitePlayer.username)+3)

# loop to listen for new connections
while True:
    print "waiting for connection..."
    newpconnection, newpaddr = socket.accept()
    newpusername = ""
    while True:
        byte = readnbytes(newpconnection, 1)
        if byte == "\n":
            break
        newpusername += byte
    newPlayer = Player(newpconnection, newpusername)
    newPlayer.start()
    # lobby.append(newPlayer)
    players.append(newPlayer)
    print(newpusername + " logged in")

socket.close()