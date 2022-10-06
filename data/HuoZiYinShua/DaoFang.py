from pydub import AudioSegment


class DaoFang:
    def process(self, filename):
        src = AudioSegment.from_file(f'audio/' + filename)
        dst = src.reverse()
        dst.export('daofang.wav', 'wav')
