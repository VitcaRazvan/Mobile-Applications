/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */


import React, { Component } from 'react';
import {
  AppRegistry,
  Alert,
  StyleSheet,
  Text,
  View,
  Navigator,
  ListView,
  TextInput,
  StatusBar,
  TouchableHighLight,
  TouchableNativeFeedback,
  TouchableOpacity,
  AsyncStorage,
  Picker
} from 'react-native';

import Communications from 'react-native-communications';
import Button from 'react-native-button';
import { BarChart } from 'react-native-charts'


var requests = []

var SCREEN_WIDTH = require('Dimensions').get('window').width;



class ReactLab5 extends React.Component {

    constructor(props){
        super(props);

        if(requests.length==0){
            this._getPersistedData();
        }

        this.state= {
            tagName: '',
            city: '',
            sprayCan: '',
            dataSource: new ListView.DataSource({
                rowHasChanged: (row1,row2) => row1 !== row2,
            }),
            loaded: false,
        }
    }

    componentDidMount(){
        this.setState({
            dataSource: this.state.dataSource.cloneWithRows(requests),
            loaded: true,
        });
    }

    _persistData(){
        return AsyncStorage.setItem('key1', JSON.stringify(requests))
                               .then(json => console.log('success! at persist save'))
                               .catch(error => console.log('error! at persist save'));
    }

    _getPersistedData(){
        return AsyncStorage.getItem('key1')
            .then(req => JSON.parse(req))
            .then(json => {
                console.log(json)

                for (var i = 0; i< json.length; i++){
                    requests.push({"tagName": json[i].tagName,"city": json[i].city,"sprayCan": json[i].sprayCan});

                    this.setState({
                        dataSource: this.state.dataSource.cloneWithRows(requests),
                        loaded: true,
                    });
                }
            })
            .catch(error => console.log('eroare la citire'));
    }

    //adds a new request into the list view

    _addBtn(){
        if(this.state.tagName !== '' && this.state.city !== '' && this.state.sprayCan != ''){
            requests.push({"tagName": this.state.tagName,"city": this.state.city,"sprayCan": this.state.sprayCan});
            Alert.alert("Request added!");
            this.setState({
                dataSource: this.state.dataSource.cloneWithRows(requests),
                loaded:true,
            });

            this._persistData();

        }else{
            Alert.alert("Inputs might be empty");
        }
    }
    //send email with the data from the list view

    _emailBtn(){
        var requestsString = requests.map(function(item){
            return "\nTag Name: "+item['tagName']+"\nCity: "+item['city']+"\nSpray Can: "+item['sprayCan'+"\n"];

        });
        Communications.emal(["vatca_razvan@yahoo.com"],"","","Sent from your app", requestsString.toSreing());
    }

    //moves to a new scene, in this case, EditDetals sending to that scene the request details
    //prefixed by "passed"

    _navigate(request){
        this.props.navigator.push({
            name: 'EditDetails',

            passProps: {
                request: request
            }
        })
    }
    //form a list view item with the properties of a new request

    renderRequest(request){
        return(

            <TouchableOpacity onPress={ () =>this._navigate(request)}>
                <View style={styles.viewDetails}>
                    <Text>{request.tagName}</Text>
                    <Text>{request.city}</Text>
                    <Text>{request.sprayCan}</Text>
                </View>
            </TouchableOpacity>
        );
    }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.header}>Welcome</Text>

        <TextInput
            style={styles.input}
            onChangeText={(text)=> this.setState({tagName: text})}
            placeholder="Tag Name"
            value={this.state.tagName}
        />
        <TextInput
            style={styles.input}
            onChangeText={(text)=> this.setState({city: text})}
            placeholder="City"
            value={this.state.city}
        />
        <TextInput
            style={styles.input}
            onChangeText={(text)=> this.setState({sprayCan: text})}
            placeholder="sprayCan"
            value={this.state.sprayCan}
        />

        <Button
            containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'green', marginBottom: 4}}
            style={{fontSize: 20, color: 'white'}}
            styleDisabled={{color: 'red'}}
            onPress={() => this._addBtn()}>
            Add request
		</Button>
        <Button
            containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'green', marginBottom: 4}}
            style={{fontSize: 20, color: 'white'}}
            styleDisabled={{color: 'red'}}
            onPress={() => this._emailBtn()}>
            Send email
		 </Button>

          <ListView
            style={styles.listView}
            dataSource={this.state.dataSource}
            renderRow={this.renderRequest.bind(this)}
          />

      </View>
    );
  }
}

class EditDetails extends React.Component{
    constructor(props){
        super(props);
        this.state={
            tagName: this.props.request.tagName,
            city: this.props.request.city,
            sprayCan: this.props.request.sprayCan
        }
    }
    _persistData(){
        return AsyncStorage.setItem('key1', JSON.stringify(requests))
                               .then(json => console.log('success! at persist save'))
                               .catch(error => console.log('error! at persist save'));
    }

    //when pressing the Save button

    _handlePressSave(){
        if(this.state.tagName !== '' && this.state.city !== '' && this.state.sprayCan != ''){
            this.props.request.tagName = this.state.tagName;
            this.props.request.city = this.state.city;
            this.props.request.sprayCan = this.state.sprayCan;

            Alert.alert("Saved!");
            this._persistData();
            this.props.navigator.pop();
        }else{
            Alert.alert("Some input might be empty");
        }
    }

    //pressing delete button

     _handlePressDelete(){
         //console.log("------------------------ DELETE start  -------------");


         var index = requests.indexOf(this.props.request);
         console.log("index = " + index);

         //console.log("current length of array: " + requests.length)
         if (index > -1) {
             requests.splice(index, 1);
             //console.log("new length of array: " + requests.length)
             Alert.alert("Done","Deleted!");

             this._persistData();

             this.props.navigator.push({
       	        name: 'ReactLab5',
             })
           }
           else{
             Alert.alert("Warning","Request not found. Can't delete");
           }
           //console.log("------------------------ DELETE gata  -------------");
     }

     _handlePressChart(){
         this.props.navigator.push({
    	        name: 'ChartPage',
         })
     }
     render() {
       return (
         <View style={styles.container}>
           <Text style={styles.header}>Welcome</Text>

           <TextInput
               style={styles.input}
               onChangeText={(text)=> this.setState({tagName: text})}
               placeholder="Tag Name"
               value={this.state.tagName}
           />
           <TextInput
               style={styles.input}
               onChangeText={(text)=> this.setState({city: text})}
               placeholder="City"
               value={this.state.city}
           />
           <TextInput
               style={styles.input}
               onChangeText={(text)=> this.setState({sprayCan: text})}
               placeholder="sprayCan"
               value={this.state.sprayCan}
           />
           <Button
             containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'green', marginBottom: 4}}
               style={{fontSize: 20, color: 'white'}}
               styleDisabled={{color: 'red'}}
               onPress={ () => this._handlePressSave() }>
               Save and return
 		      </Button>


           <Button
             containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'red', marginBottom: 4}}
               style={{fontSize: 20, color: 'white'}}
               styleDisabled={{color: 'red'}}
               onPress={ () => this._handlePressDelete() }>
               Delete
 		      </Button>

           <Button
             containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'blue', marginBottom: 4}}
               style={{fontSize: 20, color: 'white'}}
               styleDisabled={{color: 'red'}}
               onPress={ () => this._handlePressChart() }>
               Show chart
 		      </Button>
         </View>
       )
     }
}

chartDataSource=[]

class ChartPage extends React.Component{

      constructor(props){
        super(props);

        this.state = {
          top1 : '',
          top2 : '',
          top3 : ''
        }
        this._calculateChartValues();
      }

      _handlePressBack(){
          this.props.navigator.pop();
      }

      _handlePressHome(){
        this.props.navigator.push({
    	        name: 'ReactLab5',
          })
      }

      _calculateChartValues(){
          console.log("------ Am intrat in calculateChartValues()------");

          var dict = [];
          console.log("------ inainte de for------ lungimea lui requests: " + requests.length);
          for(var i = 0 ; i < requests.length ; i++){

                var found = false;

                // verific daca nu e deja in dictionar
                for(var j = 0; j < dict.length; j++) {
                    if (dict[j].key == requests[i].tagName) {
                        found = true;
                        break;
                    }
                }

                //daca nu e inca in dictionar il adaug  cu valoarea 1
                if(found == false){
                  dict.push({
                    key:   requests[i].tagName,
                    value: 1
                  });
                }else if(found == true){ // daca era deja in dictionar ii incrementez valoarea
                  for(var k = 0; k < dict.length; k++) {
                    if (dict[k].key == requests[i].tagName) {
                        dict[k].value = dict[k].value + 1
                        break;
                    }
                  }
                }
          }

          console.log("------ DICTIONARUL------ de lungime " + dict.length);
          for(var l = 0; l < dict.length; l++) {

                console.log("key: " + dict[l].key + " value: " + dict[l].value)
           }

// primul max
           var max = {
                  name: '',
                  value: 0
                }


           for(var i = 0 ; i< dict.length ; i++){
             if(dict[i].value > max.value){
               max.name = dict[i].key;
               max.value = dict[i].value;
             }
           }

           chartDataSource.push({
                  fillColor: 'red',
                  data: [{ value: max.value }]
                });

          this.state.top1 = max.name;

// al doilea max

          for( var i = 0; i< dict.length ; i++){
            if(dict[i].key == max.name){
              dict.splice(i, 1);
            }
          }
          console.log("noua lungime:" + dict.length)

          if(dict.length > 0){ // daca mai am elemente in dictionar
            var max2 = {
                  name: '',
                  value: 0
                }

            for(var i = 0 ; i< dict.length ; i++){
             if(dict[i].value > max2.value){
               max2.name = dict[i].key;
               max2.value = dict[i].value;
             }
           }

           chartDataSource.push({
                  fillColor: 'orange',
                  data: [{ value: max2.value }]
                });
          }

          this.state.top2 = max2.name;
// al treilea max

          for( var i = 0; i< dict.length ; i++){
            if(dict[i].key == max2.name){
              dict.splice(i, 1);
            }
          }


          if(dict.length > 0){ // daca mai am elemente in dictionar
            var max3 = {
                  name: '',
                  value: 0
                }

            for(var i = 0 ; i< dict.length ; i++){
             if(dict[i].value > max3.value){
               max3.name = dict[i].key;
               max3.value = dict[i].value;
             }
           }

           chartDataSource.push({
                  fillColor: 'yellow',
                  data: [{ value: max3.value }]
                });
          }
          this.state.top3 = max3.name;

      }

      render(){
      return(
        <View style={{backgroundColor: 'cyan'}}>
          <Text style={styles.header}>Chart page</Text>

           <BarChart
              dataSets= {chartDataSource}
              graduation={1}
              horizontal={false}
              showGrid={true}
              barSpacing={5}
              style={{
                height: 300,
                margin: 15,
              }}/>


              <Text ><Text style={{color: 'red'}}>Red</Text>: {this.state.top1} | <Text style={{color: 'orange'}}>Orange</Text>: {this.state.top2} | <Text style={{color: 'yellow'}}>Yellow</Text>: {this.state.top3}</Text>
              <Button
                containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'green', marginBottom: 4}}
                  style={{fontSize: 20, color: 'white'}}
                  styleDisabled={{color: 'red'}}
                  onPress={ () => this._handlePressBack() }>
                  Back
		         </Button>

            <Button
              containerStyle={{padding:10, height:45, overflow:'hidden', borderRadius:4, backgroundColor: 'red', marginBottom: 4}}
                style={{fontSize: 20, color: 'white'}}
                styleDisabled={{color: 'red'}}
                onPress={ () => this._handlePressHome() }>
                Home
            </Button>


        </View>
      )
    }
}

var App = React.createClass({

  renderScene(route, navigator) {
  	if(route.name == 'ReactLab5') {
    	return <ReactLab5 navigator={navigator} {...route.passProps}  />
    }
    if(route.name == 'EditDetails') {
    	return <EditDetails navigator={navigator} {...route.passProps}  />
    }
    if(route.name == 'ChartPage') {
    	return <ChartPage navigator={navigator} {...route.passProps}  />
    }
  },

  render() {
    return (
      <Navigator
      	style={{ flex:1 }}
        initialRoute={{ name: 'ReactLab5' }}
        renderScene={ this.renderScene } />
    )
  }
});


const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  input: {
    backgroundColor: 'white',
    height: 40,
    width: 100,
    borderColor: 'white',
    borderWidth: 1,
    margin: 3,
  },
  listView: {
      width: 320,
      paddingTop: 1,
      backgroundColor: '#F5FCFF',
    },
  header: {
    fontWeight: 'bold',
    fontSize: 30,
    textAlign : 'center',
    color: 'black'

  },
  holder: {
    flex: 0.25,
    justifyContent: 'center',
  },
  text: {
    fontSize: 50,
    backgroundColor: 'red'
  },
  viewDetails: {
	  margin: 9
  }

});

AppRegistry.registerComponent('reactLab5', () => App);

