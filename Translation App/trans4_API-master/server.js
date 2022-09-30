var express = require('express');
const bcrypt = require('bcryptjs');

var app = express();
var port = process.env.PORT || 4000;
const Translation = require('./src/models/translation.model')
const User = require('./src/models/user.model');
const login = require('./src/controller/loginController');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.get('/', function (req, res) {
  res.send({
    "Output": "Hello World!"
  });
});

app.post('/', function (req, res) {
  res.send({
    "Output": "Hello World!"
  });
});

app.get('/getAllTranslation',
  (req, res) => {
    const userId = req.query.user_id;
    Translation.getAllTranslation(userId, (err, data) => {
      if (err)
        res.status(500).send({
          success: false,
          message:
            err.message || "Some error occurred while retrieving tutorials."
        });
      else res.send(data);
    });
  }
);

app.post('/deleteTranslation', (req, res) => {
  Translation.removeTranslation(req.body.translation_id, (err, data) => {
    if (err) {

      res.status(500).send({
        success: false,
        message: "Could not delete Translation"
      });

    } else res.send({ success: true, message: `Translation was deleted successfully!` });
  });

});

app.post('/savedTranslation', (req, res) => {

  if (!req.body) {
    res.status(400).send({
      success: false,
      message: "Body cant be empty"
    });
  }

  const translation = new Translation({
    user_id: req.body.user_id,
    from_type: req.body.from_type,
    from_text: req.body.from_text,
    to_type: req.body.to_type,
    to_text: req.body.to_text,
  });

  // Save translation in the database
  Translation.saveTranslation(translation, (err, data) => {
    if (err)
      res.status(500).send({
        success: false,
        message:
          err.message || "Some error occurred while creating the Tutorial."
      });
    else res.send(data);
  });

});

app.post('/signup', async (req, res) => {
  if (!req.body) {
    res.status(400).send({
      success: false,
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
        success: false,
        message:
          err.message || "Some error occurred while creating the User."
      });
    else res.send(data);
  });
}
);

app.post('/login', login);

app.use((err, req, res, next) => {
  // console.log(err);
  err.statusCode = err.statusCode || 500;
  err.message = err.message || "Internal Server Error";
  res.status(err.statusCode).json({
    message: err.message,
  });
});

// Export your Express configuration so that it can be consumed by the Lambda handler
app.listen(port, ()=>{
  console.log(`hello Server is running on port .`);
});

module.exports = app