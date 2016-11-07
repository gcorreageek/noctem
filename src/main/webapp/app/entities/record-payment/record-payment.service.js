(function() {
    'use strict';
    angular
        .module('noctemApp')
        .factory('RecordPayment', RecordPayment);

    RecordPayment.$inject = ['$resource'];

    function RecordPayment ($resource) {
        var resourceUrl =  'api/record-payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
