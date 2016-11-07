(function() {
    'use strict';
    angular
        .module('noctemApp')
        .factory('Card', Card);

    Card.$inject = ['$resource'];

    function Card ($resource) {
        var resourceUrl =  'api/cards/:id';

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
