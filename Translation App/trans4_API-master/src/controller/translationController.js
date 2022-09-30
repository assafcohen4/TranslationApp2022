const Translation = require('../models/translation.model')

exports.saveTranslation = (req, res) =>{

    if (!req.body) {
        res.status(400).send({
            success:false,
          message: "Body cant be empty"
        });
    }

    const translation = new Translation({
        user_id: req.body.user_id,
        from_type: req.body.from_type,
        from_text: req.body.from_text,
        to_type: req.body.to_type,
        to_text:req.body.to_text,
    });
  
    // Save translation in the database
    Translation.saveTranslation(translation, (err, data) => {
      if (err)
        res.status(500).send({
            success:false,
          message:
            err.message || "Some error occurred while creating the Tutorial."
        });
      else res.send(data);
    });

}

// Delete a Translation with the specified id in the request
exports.deleteTranslation = (req, res) => {
    Translation.removeTranslation(req.body.translation_id, (err, data) => {
      if (err) {
       
          res.status(500).send({
            success:false,
            message: "Could not delete Translation"
          });
        
      } else res.send({success:true, message: `Translation was deleted successfully!` });
    });
  };

exports.getAllTranslation = (req, res) =>{
    const userId = req.query.user_id;
    Translation.getAllTranslation(userId, (err, data) => {
      if (err)
        res.status(500).send({
            success:false,
          message:
            err.message || "Some error occurred while retrieving tutorials."
        });
      else res.send(data);
    });
}