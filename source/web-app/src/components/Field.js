import React, { Component } from 'react';
import { connect } from 'react-redux';
import { fetchGetAllField, fetchDeleteField } from '../apis/field-owner-apis';
import { getAllField } from '../redux/field-owner/field-owner-action-creator'
import Header from './Header';
import Navigation from './Navigation';
function mapStateToProps(state) {
    console.log(state);
    // debugger; chay thu xme

    return {
        listField: state.listField
    };
}

//@connect(mapStateToProps, null)
class Field extends Component {

    componentDidMount() {
        fetchGetAllField(1).then(data => this.props.getAllField(data));
        
    }

    deleteField(fieldId) {
        fetchDeleteField(fieldId)
        .then(
            fetchGetAllField()
            .then(data => this.props.getAllField(data))
        );
    }

    render() {
        
        const { listField } = this.props;
        const renderField = listField.map(
            <tr>
                <td>{listField.fieldName}</td>
                <td>{listField.fieldType}</td>
                <td>
                    <button className="btn btn-info">Update</button>
                    <button onClick={this.deleteField(listField.fieldId).bind(this)} className="btn btn-danger">Delete</button>
                </td>
            </tr>
        );
        return (
            <div>
                <Header />
                <Navigation />
                <div id="page-wrapper">
                    <div className="container-fluid">
                        <div className="row">
                            <div className="col-lg-4">
                                <h2 className="page-header">Field</h2>
                            </div>
                        </div>
                        <div className="col-lg-12">
                            <form className="form-horizontal">
                                <div className="form-group">
                                    <label for="inputEmail3" className="col-sm-3 control-label">Field Name</label>
                                    <div className="col-sm-9">
                                        <div className="row">
                                            <div className="col-sm-6">
                                                <input type="text" className="form-control" id="inputPassword3" placeholder="Field name" />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div className="form-group">
                                    <label for="sel1" className="col-sm-3 control-label">Field Type</label>
                                    <div className="col-sm-2">
                                        <select className="form-control" id="sel1">
                                            <option>5 vs 5</option>
                                            <option>7 vs 7</option>
                                        </select>
                                    </div>

                                </div>

                                <div className="form-group">
                                    <div className="col-sm-offset-3 col-sm-9">
                                        <button type="submit" className="btn btn-primary">Create</button>
                                    </div>
                                </div>

                            </form>
                        </div>
                        <div className="col-lg-8 col-lg-offset-2">
                            <div className="table-responsive">
                                <table className="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Field Name</th>
                                            <th>Field Type</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                        {listField ? renderField : "There is no field"}


                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}


export default connect(mapStateToProps, { getAllField })(Field);
//export default Field;