import { defineStore } from 'pinia'

export const usePlayerStore = defineStore('player', {
  state: () => ({
    currentSong: null,
    isPlaying: false,
    volume: 0.5,
    progress: 0,
    duration: 0,
    playlist: []
  }),
  actions: {
    setCurrentSong(song) {
      this.currentSong = song
    },
    play() {
      this.isPlaying = true
    },
    pause() {
      this.isPlaying = false
    },
    togglePlay() {
      this.isPlaying = !this.isPlaying
    },
    setVolume(volume) {
      this.volume = Math.max(0, Math.min(1, volume))
    },
    setProgress(progress) {
      this.progress = Math.max(0, Math.min(1, progress))
    },
    setDuration(duration) {
      this.duration = duration
    },
    addToPlaylist(song) {
      this.playlist.push(song)
    },
    removeFromPlaylist(songId) {
      this.playlist = this.playlist.filter(song => song.id !== songId)
    },
    clearPlaylist() {
      this.playlist = []
    }
  }
})