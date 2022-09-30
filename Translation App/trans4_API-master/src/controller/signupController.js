const bcrypt = require('bcryptjs');
const User = require("../models/user.model");

exports.signup = async (req, res) => {

  // Validate request
  if (!req.body) {
    res.status(400).send({
      success:false,
      message: "Content can not be empty!"
    });
  }


  const hashPass = await bcrypt.hash(req.body.password, 10);


  // Create a User
  const user = new User({
    email: req.body.email,
    password: hashPass
  });

  // Save User in the database
  User.createUser(user, (err, data) => {
    if (err)
      res.status(500).send({
        success:false,
        message:
          err.message || "Some error occurred while creating the User."
      });
    else res.send(data);
  });




};
