Savant
-----
This is the Savant, the statistics service. The backend service (Java) puts a tailable cursor onto the oplog of a targetted database. Oplogs are created when a database uses replica sets and capture all the actions that occur within a database. This is a more reliable way of capturing data rather that look directly into a collection because of the discrepancies between when an action occurs and when the action is logged. The tailable cursor does not expire, so it will continuously read off the database until the program is ended.  

The data is then aggregated into time slots under different categories in a way that is meaningful to the business and stored into a separate collection on the server. This collection is accessed by the front end by utilizing REST endpoints to call methods. The data is sent to the UI in the form of a JSON.

The UI can sort the data by different categories that each data was a part of or by the time stamp of the data. The graph uses KendoUI to display the graph. The UI will capture any updates from the database (new data) and automatically make the necessary changes on the graph in real-time.



￼The Savant
OplogDataCollector - Creates a tailable cursor (never expires) on the targetted server's oplog.rs. Reads oplog collection for inserts and updates (but not removals). After giving it the targetted database, the program will start tailing actions that occur within that database. The data is then written into targeted database where the resources will pull data from. Information is stored into 3 collections listed below.

1. mongo tsData - the document keeps track of the last oplog document was processed.

2. mongo GeneralData - the document stores all objects processed with the createdDtm field and a
specified targetted field (eg. delivery profile)

3. mongo GraphData - the document aggregates the count for specified statuses and clients and delivery profiles in time increments of 5 minutes, 10 minutes, 30 minutes, 1 hour, and 1 day. The data is grouped into 7 different categories, the different combinations of status, client, and delivery profile (eg. status and client, status and client and delivery profile, delivery profile ... etc.).

Resource -

1. /request/db - POST - targeted database to collect data from. The resource keeps track of what databases were entered. In the case of a repeat, the program will do nothing.

2. /request/view - GET - shows the databases that tailable cursors exist in.

3. /statistics - POST - postString is a JSON string with lowerTime, upperTime, size, client, profile. Data is returned by filtering for createdDtm
between the upper and lower values, for size, for client(s), and for delivery profile(s). Clients and delivery profiles are stored into an
array.

4. Other POST endpoints are the following:
/statistics, /client_deliveryprofile, /statistics, /status_deliveryprofile, /statistics, /status_client, /statistics/status, /statistics, /client, /statistics/deliveryprofile
￼￼￼￼￼￼
