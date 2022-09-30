const conn = require("../config/db");

const Translation = function (translation) {
    this.user_id = translation.user_id;
    this.from_type = translation.from_type;
    this.from_text = translation.from_text;
    this.to_type = translation.to_type;
    this.to_text = translation.to_text;
};

Translation.saveTranslation = (newTranslation, result) => {
    conn.query("INSERT INTO translation SET ?", newTranslation, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }

        console.log("saved Translation: ", { id: res.insertId, ...newTranslation });
        result(null, { success: true, message: "Translations saved successfully.", saved_translation: { id: res.insertId, ...newTranslation } });
    });
};

Translation.getAllTranslation = (userId, result) => {
    let query = "SELECT * FROM translation where user_id = ?";
    conn.query(query, userId, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        result(null, { success: true, message: "All saved translations get successfully.", translation_list: res });
    });
};

Translation.getStaticTranslation = (result) => {
    let query = "SELECT * FROM translation";
    conn.query(query, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(null, err);
            return;
        }
        result(null, { success: true, message: "All saved translations get successfully.", translation_list: res });
    });
};

Translation.removeTranslation = (translationId, result) => {
    conn.query("DELETE from translation where translation_id = ?", translationId, (err, res) => {
        if (err) {
            console.log("error: ", err);
            result(err, null);
            return;
        }

        if (res.affectedRows == 0) {
            // not found Tutorial with the id
            result({ success: false, message: "not_found" }, null);
            return;
        }
        console.log("deleted translation with id: ", translationId);
        result(null, { success: true, message: "Translation delete successfully.", res });
    });
};


module.exports = Translation;