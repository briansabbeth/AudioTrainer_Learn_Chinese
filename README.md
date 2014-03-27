CSC-499-Capstone-Project-1
==========================
An application to assist in the learning of Chinese by playing an assortment of audio clips in Mandarin which the user would interact with. 

I specifically designed and implemented the backend storage and retrieval for this application using SQLite for Android as well as created a weighted probability model that would increase and decrease the likelihood of repeating certain clips, which were difficult for the user. In this application a user plays an assortment of clips that are chosen at random and has the ability to repeat them. Clips that were repeated at a given frequency would have a higher chance of being selected in the future. Probability was stored on the users device over time via the database and its surrounding infrastructure. The backend also handled the display, repeating and updatng of the applications history and the selection of the number of clips, accessed over a set number of minutes. 
