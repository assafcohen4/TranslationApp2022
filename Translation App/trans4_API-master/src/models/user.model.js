const conn = require("../config/db")

const User = function (user) {
  this.email = user.email;
  this.password = user.password;
};

const validateEmail = (email) => {
  return new Promise(async (resolve, reject) => {
    await conn.query(
      "SELECT * FROM `users` WHERE `email`=?",
      [email], (err, res) => {
        if (err) {
          console.log("error: ", err);
          result(err, null);
          return;
        }

        if (res.length > 0) {
          resolve(true)
        }
        else {
          resolve(false)
        }
      })
  })
}

User.createUser = async (newUser, result) => {

  const isEmailExist = await validateEmail(newUser.email);

  if (isEmailExist) {
    result(null, { success: false, message: "The E-mail already in use", });

  } else {
    conn.query("INSERT INTO `users` SET ?", newUser, (err, res) => {
      if (err) {
        console.log("error: ", err);
        result(err, null);
        return;
      }

      result(null, { success: true,  message: "User signed up successfully.", user : { user_id: res.insertId, email : newUser.email  } });
    });
  }
 
};

module.exports = User;