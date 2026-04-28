As of v1.0.2.3, there is a new Team Manager screen that allows you to create & manage teams for use as an easy method of grouping allies for your group. You can open the Team Manager screen by pressing the Team Manager button in the top-right of your inventory (can be moved in the client config).

## How Teams Work
Once a team has been created in the Team Manager screen, they can be defined as a traders owner by any member of the team in the traders [ownership settings](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ownership-tab). They can also be defined as the owner of various other machines that have their own versions of ownership selection.

Once the trader is owned by that team, team admins/owners will have owner-level permissions for every trader owned by the team.

Team members will be given Ally-level permissions for every trader owned by that team, as defined in each traders [ally permissions](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ally-permissions-tab), and may/will vary between different traders.

## Team Manager Screen
Like the Trader Settings Screen, the Team Manager screen consists of several tabs.

### Team Selection Tab
![](https://i.imgur.com/WF1BgLp.png)
* The team selection tab is where you select which team to view/edit. A list of teams that you are at least a member of will appear in the top-middle of the screen. If you are a member of more than 5 teams, you can use the scroll-wheel to scroll through the list.
* Clicking on a team will select that team, and the other tabs will appear at the top as appropriate for what you're allowed to do within that team.
* If you'd like to make a new team, simply type a team name into the text box at the bottom, and click the Create button. The team will then be created and auto-selected (may take a few seconds if you have high ping, as it need to wait for the server to actually create it and inform you of the new team's ID).

### Team Member Tab
![](https://i.imgur.com/U4wRo1t.png)

The team member tab allows you to view all of the teams members. The Team Owners name appears in green, Team Admins appear in dark-green, and all other Members will appear in white.

### Team Member Edit Tab
![](https://i.imgur.com/DfQfvXc.png)

The team member edit tab allows you to add/remove team members as well as adding/promoting members to admin level. This tab can be accessed by all members, however they can only access features up to their level.

* Members can only remove themselves from the team by typing in their own name and clicking the "Remove" button
* Admins can add/remove any non-admins to/from the members list. They can also demote themselves to a member by typing in their name and clicking the "Set Admin" button
* Owner can promote any member to an admin, or demote existing admins by typing in their name and clicking the "Set Admin" button.

### Team Bank Account Tab
![](https://i.imgur.com/kQQx34f.png)
* The team bank account tab allows you to create a bank account for this team, and define which members are allowed to access the bank account.
* Can only be accessed by the Team's Owner.
* Bottom button allows you to toggle which members have access to the teams bank account. Defaults to Owner Only, but can be set to Admins Only, or to all Members.
* Note that regardless of the permissions here, any member with permission to link a bank account to a trader owned by the team will be able to do so.
* Team Bank Accounts balance is shown below the Create Bank Account button

### Team Salary
Each Team has the ability to grant a salary to all members periodically from the funds in the Teams Bank Account. The following tabs are all part of this ability:

#### Salary Info Tab
![](https://i.imgur.com/50OF8Hp.png)

This tab is accessible to all Members, and simply displays a summary of the current Salary Settings such as how much each player gets paid, how frequently they'll get paid, how much money the bank account must have in order to successfully pay the salary, etc.

Only admins will be able to see the full details however, and members will only be able to see how much they get paid and when the next payment will be.

A large red notification will appear at the bottom if a salary trigger fails due to lack of funds, and will only clear once a salary has been successfully paid.

#### Salary Settings Tab
![](https://i.imgur.com/XFWRJ8h.png)

This tab contains the majority of the settings regarding the Salary functionality, and as such is only accessible to Team Admins. The large button at the top Enables/Disables the Auto-Salary mode, with the time input below determining how frequently the salary will be paid.

The "Push Notification" checkbox determines whether to push the salary payment notification to their [Personal Notifications](https://github.com/Lightman314/LightmansCurrency/wiki/Notifications) or not.

The "Trigger Salary Payment" button allows admins to manually trigger a salary payment, and will not in any way reset the timer for the next Auto-Salary if it's enabled (if you wish to reset this timer manually, simply disable and then re-enable the auto-salary). This is useful for those who wish to ignore the Auto-Salary functionality and only trigger it when they wish to, or for those who wish to make up for an Auto-Salary trigger that failed due to lack of funds.

#### Salary Payment Tab
![](https://i.imgur.com/ylrUh29.png)

This tab allows you to define the salary paid to each member/admin, and as such is only accessible to Admins.

The Member Salary input defines how much money will be paid to each member. By default this amount will also be paid to each Admin as well unless the Admin Salary is flagged as separate from those of a normal member.

The "Separate Admin Salaries" toggle enables the Admin Salary input, and makes it so that admins are no longer paid the Member Salary.

If the Admin Salary is separated from the Member Salary, the Salary trigger will still function even if one of the two salaries is undefined. There is also no requirement that the two salaried be the same type of money (so admins can be paid in Chocolate Coins, whereas members are paid in Normal Coins, etc.)

If you are in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode), a Creative Toggle button will appear in the top-right corner. Clicking this will enable/disable creative salaries. While a team has Creative Salaries enabled, the salaries will be paid without taking out or requiring money in the Teams Bank Account.

Note: Only the Teams Owner can access the Admin Salary inputs, so admins can't change their own salaries.

### Team Statistics Tab
![](https://i.imgur.com/jAPJYTu.png)

This tab displays all Team related stats. I should note that any stats recorded by a Trader that's owned by this team will also be passed along to the team, making it a convenient place to keep trade of the teams overall sales & purchases.

### Name & Ownership Tab
![](https://i.imgur.com/gVJgd0H.png)

This tab allows Admins to change the Teams name, and also allows the Teams Owner to either Transfer the teams ownership to another player or to disband the team entirely

When the owner transfers ownership to another player, the former owner will be added as a team admin

**Warning**: When a team is disbanded, all machines owned by that team will revert their ownership to the most recent player owner

For debug purposes, the Teams numerical ID is also displayed at the bottom of this page for use by any relevant team-targeting commands