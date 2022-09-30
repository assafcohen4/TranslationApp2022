const { validationResult } = require('express-validator');
const bcrypt = require('bcryptjs');
const conn = require('../config/db');

module.exports = async (req, res, next) => {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
        return res.status(422).json({ errors: errors.array() });
    }

    try {

        let sqlQuery = "SELECT * FROM users WHERE email=?";


        conn.query(sqlQuery, [req.body.email],async (err, results) => {
            if (err) {
              console.log("error: ", err);
              result(err, null);
              return;
            }
            
            if (results.length > 0) {

                const passMatch = await bcrypt.compare(req.body.password, results[0].password);
                if(!passMatch){
                    return res.status(200).json({
                        success: false ,
                        message: "Incorrect password",
                    });
                }
                else{
                    return res.status(200).json({
                        success:true,
                        message:"User logged in successfully.",
                        user:{
                            user_id:results[0].user_id,
                            email:results[0].email
                        }
                    });
                }
                
                return;
            }
            else{

              // If user is not found in database
              res.status(200).json({ success: false , message :  "User not found." });
            }
            
          });

    }
    catch (err) {
        next(err);
    }
}
