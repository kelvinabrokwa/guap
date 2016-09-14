package main

import (
    "os"
    "fmt"
    "log"
    "time"
    "net/http"
    "database/sql"
    "encoding/json"
    _ "github.com/lib/pq"
)

var db *sql.DB
const url = "https://api.bitcoinaverage.com/ticker/global/USD/"

type Data struct {
    Avg_24h float64 `json:"24h_avg"`
    Ask float64 `json:"ask"`
    Bid float64 `json:"bid"`
    Last float64 `json:"last"`
    Timestamp string `json:"timestamp"`
    Volume_btc float64 `json:"volume_btc"`
    Volume_percent float64 `json:"volume_percent"`
}

func fetch() {
    res, err := http.Get(url)
    if err != nil {
        log.Println(err)
    }
    defer res.Body.Close()
    data := Data{}
    json.NewDecoder(res.Body).Decode(&data)
    _, err = db.Query(`
        INSERT INTO
          prices
          (avg_24h, ask, bid, last, time, volume_btc, volume_percent)
        VALUES
          ($1, $2, $3, $4, $5, $6, $7)
    `, data.Avg_24h, data.Ask, data.Bid, data.Last, data.Timestamp, data.Volume_btc, data.Volume_percent)
    if err != nil {
        log.Println(err)
    }
}

func main() {
    var err error
    db, err = sql.Open("postgres", fmt.Sprintf("postgres://%s:%s@localhost/guap?sslmode=disable", os.Args[1], os.Args[2]))
    if err != nil {
        log.Panic(err)
    }
    ticker := time.NewTicker(1 * time.Second)
    for {
        select {
        case <- ticker.C:
            go fetch()
        }
    }
}
