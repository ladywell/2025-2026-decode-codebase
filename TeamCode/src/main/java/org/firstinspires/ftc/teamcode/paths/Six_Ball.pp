{
  "version": "1.7.5",
  "header": {
    "info": "Created with Pedro Pathing Plus Visualizer",
    "copyright": "Copyright 2026 Matthew Allen. Licensed under the Modified Apache License, Version 2.0.",
    "link": "https://github.com/Mallen220/PedroPathingPlusVisualizer"
  },
  "startPoint": {
    "x": 21.289126335637967,
    "y": 124.36643620364549,
    "heading": "linear",
    "startDeg": 137.60379482473468,
    "endDeg": 145,
    "locked": false
  },
  "lines": [
    {
      "id": "mmma3e43-iealku",
      "name": "",
      "endPoint": {
        "x": 57.31798561151079,
        "y": 91.47194244604316,
        "heading": "tangential",
        "reverse": true
      },
      "controlPoints": [],
      "color": "#889DB5",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mmmas4xa-wz21vf",
      "name": "",
      "endPoint": {
        "x": 36.13669064748202,
        "y": 84.41438848920862,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 180
      },
      "controlPoints": [],
      "color": "#A5ABDC",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mmmau5h9-s6tbxe",
      "name": "",
      "endPoint": {
        "x": 14,
        "y": 84.41438848920862,
        "heading": "tangential",
        "reverse": false
      },
      "controlPoints": [],
      "color": "#A56BB5",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mmmauqrq-jlko29",
      "name": "",
      "endPoint": {
        "x": 57.31798561151079,
        "y": 91.47194244604316,
        "heading": "linear",
        "reverse": false,
        "startDeg": 145,
        "endDeg": 145
      },
      "controlPoints": [],
      "color": "#8BD869",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "mmma3e43-iealku"
    },
    {
      "kind": "wait",
      "id": "mmma45eo-bjzr3v",
      "name": "",
      "durationMs": 1000,
      "locked": false
    },
    {
      "kind": "path",
      "lineId": "mmmas4xa-wz21vf"
    },
    {
      "kind": "path",
      "lineId": "mmmau5h9-s6tbxe"
    },
    {
      "kind": "path",
      "lineId": "mmmauqrq-jlko29"
    },
    {
      "kind": "wait",
      "id": "mmmaw70b-1slvd1",
      "name": "",
      "durationMs": 1000,
      "locked": false
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        {
          "x": 144,
          "y": 70
        },
        {
          "x": 144,
          "y": 144
        },
        {
          "x": 118,
          "y": 144
        },
        {
          "x": 138,
          "y": 118
        },
        {
          "x": 138,
          "y": 70
        }
      ],
      "color": "#dc2626",
      "fillColor": "#fca5a5",
      "type": "obstacle"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        {
          "x": 7,
          "y": 118
        },
        {
          "x": 26,
          "y": 144
        },
        {
          "x": 0,
          "y": 144
        },
        {
          "x": 0,
          "y": 70
        },
        {
          "x": 7,
          "y": 70
        }
      ],
      "color": "#0b08d9",
      "fillColor": "#fca5a5",
      "type": "obstacle"
    }
  ],
  "extraData": {}
}