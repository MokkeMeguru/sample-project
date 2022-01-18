#!/usr/bin/env python3
import asyncio
from gql import gql, Client
from gql.transport.aiohttp import AIOHTTPTransport

import requests

Endpoint = "http://talking.private-function.app.internal:8888/api"


def talking_app_server(event, context):
    print("Received", context.event_id)
    print("raw event", event)
    print("raw context", context)
    try:
        talking2()
        talking()
    except Exception as e:
        print(f"unknown error '{e}'")


def talking():
    transport = AIOHTTPTransport(url=Endpoint)
    client = Client(
        transport=transport,
        fetch_schema_from_transport=True,
        execute_timeout=20,
    )
    query = gql(
        """
query GetByID($id: ID) {
    game_by_id(id: $id) {
    description
    id
    max_players
        name
        play_time
        summary
    }
}
            """
    )
    params = {"id": "1234"}
    print("run fetch")
    result = client.execute(query, variable_values=params)
    print(result)


def talking2():
    print("request to ", Endpoint)
    headers = {"Content-Type": "application/json"}
    data = '{"query":"query GetByID($id: ID) {\n  game_by_id(id: $id) {\n    description\n    id\n    max_players\n    name\n    play_time\n    summary\n  }\n}\n","variables":{"id":"1234"},"operationName":"GetByID"}'
    response = requests.post(Endpoint, data=data, headers=headers, timeout=(10.0, 17.5))
    print(response)


if __name__ == "__main__":
    Endpoint = "http://localhost:8080/api"
    print("run talking")
    talking()
