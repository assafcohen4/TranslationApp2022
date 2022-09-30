// const router = require('express').Router();
// const {body} = require('express-validator');
// const {signup} = require('../controller/signupController');
// const {login} = require('../controller/loginController');
// const {getUser} = require('../controller/getUserController');



// router.get('/getuser',getUser);
var router = require("express").Router();
const { body } = require('express-validator');
const {signup} = require('../controller/signupController');
const { login } = require('../controller/loginController');
const { saveTranslation,getAllTranslation,deleteTranslation } = require('../controller/translationController');

module.exports = app => {
  router.post("/signup", signup);
  router.post('/login',login);
  router.post('/saveTranslation',saveTranslation);
  router.get('/getAllTranslation',getAllTranslation);
  router.post('/deleteTranslation',deleteTranslation);
  app.use('/api/v1', router);
};