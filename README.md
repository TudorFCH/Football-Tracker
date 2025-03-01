# Football-Tracker

Football Score & Player Stats Tracker 

 

A Java-based terminal application for tracking football match scores and managing player statistics. This application allows users to input match scores and track the stats of favorite players throughout the season. 

 

Features 

 

	1.	Football Score Tracking: 

	•	Input scores and events for matches. 

	•	Filter match scores by team for focused updates. 

	•	View a summary of match events, including goals, assists, cards, and substitutions. 

	2.	Player Stats Management: 

	•	Add and manage player statistics (e.g., goals, assists, appearances). 

	•	Compare stats between players for a quick performance overview. 

	3.	Additional Functionalities: 

	•	Save and Load Data: Persist data for matches and players across sessions. 

	•	Basic Analysis: View summaries and comparisons of player stats. 

 

Usage 

 

1. Football Score Tracking 


	•	Enter live match scores, and filter matches by team to view relevant games. 

	•	After a match concludes, view the summary report for key events (e.g., goals, cards). 

 

 

 

2. Player Stats Management 

	•	In the “Player Stats” section, add new players and enter their statistics. 

	•	View individual player summaries or compare stats between two players. 

	•	Save data to a file to retain it between sessions. 

 

 

Data Model 

 

The data model includes: 

	•	Match: Stores match information such as teams, score, and events. 

	•	Player: Tracks player-specific data including name, goals, assists, and appearances. 

	•	Team: Manages a collection of players and handles filtering options for match tracking. 

 
![image](https://github.com/user-attachments/assets/a64b3e6e-3b74-411a-8a04-71270fcd81e2)

Relationships Summary
The relationships among the entities are represented as follows:
- 1:1 between Player and PlayerStats.
- 1:M between League and Team, Team and Player, Match and Event, and ScoreTracker and Match, Match and Team, Notification and Match.
- M:N relationship between User and Team via FavoriteTeam entity.
This structure ensures that each aspect of the Football Score & Player Stats Tracker is logically connected.



 
