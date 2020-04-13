module.exports.CHAT = {
    // Connexion
    CONNECTION: "connection",
    DISCONNECTION: "disconnection",

    // Canaux de discussion
    CHANNELS: "channels",
    DELETE_CHANNEL: "deleteChannel",
    JOIN_CHANNEL: "joinChannel",
    LEAVE_CHANNEL: "leaveChannel",
    NEW_CHANNEL: "newChannel",

    // Message
    MESSAGE: "chat message",
    HISTORY: "history",
}

module.exports.DRAFT = {
    DRAFTSMAN_DIMENSION: "draftDimension",
    STROKE_COLLECTED: "StrokeCollected",
    STROKE_ERASING: "StrokeErasing",
    STROKE_SEGMENT_ERASING: "SegmentErasing",
    STROKE_COLOR: "CouleurSelectionnee",
    STROKE_DRAWING: "StrokeDrawing",
    STROKE_SIZE: "TailleTrait",
    STROKE_TIP: "PointeSelectionnee",
    STROKE_TOOL: "OutilSelectionne",
}

module.exports.MATCH = {
    ANSWER: "answer",
    FULL: "fullMatch",
    JOIN_MATCH: "joinGame",
    REMAINING_TIME: "remainingTime",
    START_MATCH: "startMatch",    
    START_ROUND: "startTimer",
    NEXT_ROUND: "nextRound",
}